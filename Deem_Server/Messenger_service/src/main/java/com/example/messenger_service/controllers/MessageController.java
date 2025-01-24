package com.example.messenger_service.controllers;

import com.example.messenger_service.config.PersonDetails;
import com.example.messenger_service.dao.ChatDAO;
import com.example.messenger_service.models.CreateMessageDTO;
import com.example.messenger_service.models.Message;
import com.example.messenger_service.services.ChatService;
import com.example.messenger_service.services.MessageService;
import com.example.messenger_service.services.MessengerServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import static com.example.messenger_service.util.ResponseValidator.getErrors;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessengerServiceClient messengerServiceClient;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ChatDAO chatDAO;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid CreateMessageDTO dto,
                                      BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(getErrors(bindingResult));

        Message message = modelMapper.map(dto, Message.class);

        //проверим существует ли чат этого сообщения или нет
        if (dto.isNewChat()) { //создаем новый чат если не существует
            List<Message> messageList = new ArrayList<>(); //TODO на случай проверить account_chat на наличие переписки
            messageList.add(message);
            dto.getChat().setMessages(messageList);
            chatService.CreateNewChat(dto.getChat());
        }

        Message actualObject = messageService.save(message);

        if (message.getImages() != null)
            if (message.getImages().size() != 0) {
                message.getImages().forEach(x -> x.setId_message(actualObject.getId()));

                try {
                    messengerServiceClient.addImagesNews(message.getImages());
                } catch (Exception e) {
                    messageService.delete(actualObject);
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }

        //send
        messengerServiceClient.pushMessageTo(message); //отправляем запрос на уведомления

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getMessage")
    public Message getMessage(int id) {
        return messageService.getMessage(id);
    }

    @GetMapping("/getMessagesFeed")
    public List<Message> getMessagesFeed(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") Date date,
                                  @RequestParam("chatId") Long chatId,
                                  @AuthenticationPrincipal PersonDetails personDetails) {
        //Проверим принадлежит ли данный чат пользователю.
        if (!chatDAO.IsThereSuchAChat(personDetails.getId(), chatId))
            return null;

        return messageService.getMessagesFeed(date, chatId);
    }

    /** Отправить новые сообщения. Обновление чата. Пользователь отправляет
     * дату последнего сообщения в кэше его устройства на сервер, сервер же
     * определяет является ли сообщение последним и если нет отправляет новые
     * сообщения клиенту*/
    @GetMapping("/updateMessages")
    public List<Message> getLastMessages(String dateLastMessage, int idChat) {

        return new ArrayList<>();
    }

}

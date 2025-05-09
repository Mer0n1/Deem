package com.example.messenger_service.controllers;

import com.example.messenger_service.config.PersonDetails;
import com.example.messenger_service.dao.ChatDAO;
import com.example.messenger_service.models.Chat;
import com.example.messenger_service.models.CreateMessageDTO;
import com.example.messenger_service.models.Image.MessageImage;
import com.example.messenger_service.models.Message;
import com.example.messenger_service.services.ChatService;
import com.example.messenger_service.services.MessageService;
import com.example.messenger_service.services.MessengerServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    @Mock
    private ChatService chatService;

    @Mock
    private ChatDAO chatDAO;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void sendMessage_ItsNewChat_NoImages() throws JsonProcessingException {
        BindingResult bindingResult = mock(BindingResult.class);
        CreateMessageDTO dto = new CreateMessageDTO();
        dto.setNewChat(true);
        dto.setChat(new Chat());
        Message message = new Message();
        message.setImages(new ArrayList<>());
        message.setChat(new Chat());
        message.getChat().setId(1L);
        message.getChat().setUsers(new ArrayList<>());

        when(modelMapper.map(dto, Message.class)).thenReturn(message);
        doNothing().when(chatService).CreateNewChat(any());
        when(messageService.save(message)).thenReturn(message);
        doNothing().when(messageService).doImage(any(), any());
        doNothing().when(messageService).pushMessage(any(), any());
        when(chatService.getChat(message.getChat().getId())).thenReturn(message.getChat());

        ResponseEntity<?> response = messageController.sendMessage(dto, bindingResult, new PersonDetails());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(chatService).CreateNewChat(dto.getChat());
        verify(messageService).save(message);
        verify(messageService).pushMessage(message, message.getChat().getUsers());
    }

    @Test
    void sendMessage_ItsNotANewChat() throws JsonProcessingException {
        BindingResult bindingResult = mock(BindingResult.class);
        CreateMessageDTO dto = new CreateMessageDTO();
        dto.setNewChat(true);
        dto.setChat(new Chat());
        Message message = new Message();
        message.setImages(new ArrayList<>());
        message.setChat(new Chat());
        message.getChat().setId(1L);
        message.getChat().setUsers(new ArrayList<>());

        when(modelMapper.map(dto, Message.class)).thenReturn(message);
        when(messageService.save(message)).thenReturn(message);
        doNothing().when(messageService).doImage(any(), any());
        doNothing().when(messageService).pushMessage(any(), any());
        when(chatService.getChat(message.getChat().getId())).thenReturn(message.getChat());

        ResponseEntity<?> response = messageController.sendMessage(dto, bindingResult, new PersonDetails());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(messageService).save(message);
        verify(messageService).pushMessage(message, message.getChat().getUsers());
    }

    /*@Test
    void getMessage() {
        int id = 1;

        when(messageService.getMessage(id)).thenReturn(new Message());

        Message message = messageController.getMessage(id);

        verify(messageService).getMessage(id);
        assertNotNull(message);
    }*/

    @Test
    void getMessagesFeed() {
        Date date = new Date(System.currentTimeMillis());
        Long chatId = 1L;
        PersonDetails personDetails = new PersonDetails();
        personDetails.setId(2L);
        boolean resultMeth = true;

        when(chatDAO.IsThereSuchAChat(personDetails.getId(), chatId)).thenReturn(resultMeth);
        when(messageService.getMessagesFeed(date, chatId)).thenReturn(new ArrayList<>());

        List<Message> list = messageController.getMessagesFeed(date, chatId, personDetails);

        verify(chatDAO).IsThereSuchAChat(personDetails.getId(), chatId);
        verify(messageService).getMessagesFeed(date,chatId);

        assertNotNull(list);
    }

    @Test
    void getMessagesFeedReturnNull() {
        Date date = new Date(System.currentTimeMillis());
        Long chatId = 1L;
        PersonDetails personDetails = new PersonDetails();
        personDetails.setId(2L);
        boolean resultMeth = false;

        when(chatDAO.IsThereSuchAChat(personDetails.getId(), chatId)).thenReturn(resultMeth);

        List<Message> list = messageController.getMessagesFeed(date, chatId, personDetails);

        verify(chatDAO).IsThereSuchAChat(personDetails.getId(), chatId);
        assertNull(list);
    }


}
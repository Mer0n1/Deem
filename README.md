# Deem

 Deem - проект электронного журнала по новой специальной образовательной программе "Делигизм". Электронный журнал предназначен для университетского образования, а не для школы.

Основная идея Deem - личные баллы вместо оценок. Баллы можно зарабатывать на экзаменах. После прохождения экзаменов администрация должна зачислить нужное количество баллов студенту. 
Существуют средние баллы группы - это средние сумма баллов всех учащихся группы. В соответствиии со средними баллами группы определяется место группы в топе факультета и курса и в топе университета. 

### Запуск
На данный момент сервер недоступен, но в будущем планируется запуск в Google Play Market.

По идее электронного журнала любая регистрация происходит администрацией (разработчиками приложения).

Однако есть возможность протестировать приложение гостевыми аккаунтами:
Логины: Test Test1
Пароль: 123456

Также есть комплект jar файлов. Необходимо запустить bash файл start.sh. 
Ссылка: https://disk.yandex.ru/d/JGLIQJLDRyr1HA


### Использование в IDLE
Для запуска сервера необходимо иметь SQL БД. Все настройки таблиц находятся внутри папки docs в файле dilichi. Достаточно будет импортировать этот файл в БД.

Каждый сервис должен содержать файл application.properties. Настройки этого файла находятся внутри папки docs. Для начала работы необходимо сгенерировать секретный код и jwt ключ. 

Я предлагаю использовать начальный jwt ключ сгенерированный мною: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJ1c2VybmFtZSI6IlRhbyIsImlkIjo0LCJST0xFIjoiUk9MRV9ISUdIIiwiY291cnNlIjoxLCJmYWN1bHR5IjoiRVBGIiwiaWF0IjoxNzAyNjM0NzUyLCJpc3MiOiJtZXJvbmkiLCJleHAiOjIwNjI2MzQ3NTJ9.5YdyXgKdeyigRDfc7kTjKUW8gKcfItg4YQon_rPtnwM

И секретный код:
5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437


На данный момент не рекомендуется изменять адреса и порты так как необходимые порты и адреса прописаны в RestTemplate-сервисах.

Можно также посмотреть подробную документацию методов на Swagger (если сможете подключить)


### Превью
<img src="img/1.jpg" alt="drawing" width="200"/> <img src="img/2.jpg" alt="drawing" width="200"/>
<img src="img/3.jpg" alt="drawing" width="200"/>
<img src="img/4.jpg" alt="drawing" width="200"/>
<img src="img/5.jpg" alt="drawing" width="200"/>
<img src="img/6.jpg" alt="drawing" width="200"/>
<img src="img/7.jpg" alt="drawing" width="200"/>
<img src="img/8.jpg" alt="drawing" width="200"/>
<img src="img/9.jpg" alt="drawing" width="200"/>
<img src="img/10.jpg" alt="drawing" width="200"/>
<img src="img/11.jpg" alt="drawing" width="200"/>
<img src="img/12.jpg" alt="drawing" width="200"/>
<img src="img/13.jpg" alt="drawing" width="200"/>

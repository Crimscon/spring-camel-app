# spring-camel-app

### Формат получения данных адаптером:
```
{
  "message": String,
  "lang": ENUM (RU, ES, EN),
  "coordinate": {
  	"lat": String,
  	"lon": String
  }
}
```
---

### Формат отправки данных: 
```
{
    "message": String,
    "dateTime": LocalDateTime (yyyy-MM-ddTHH:mm:ss.SSSSS),
    "temperature": Integer
}
```

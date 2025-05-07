# ☕ CoffeeMark — Android додаток для кав'ярень

**CoffeeMark** — це безпечний мобільний додаток для взаємодії клієнтів та барист у мережі кав'ярень. Додаток підтримує шифрування даних, обмін ключами, пошук закладів, генерацію та обробку QR-кодів, збереження локальних даних та інші функції.

---

## 🔐 Основні можливості

* **Реєстрація та авторизація з RSA-шифруванням**

  * Клієнт перевіряє наявність публічного ключа сервера при запуску.
  * Якщо ключ відсутній — надсилає запит на отримання.
  * Дані шифруються публічним ключем сервера.

* **Ролі користувачів**

  * `CLIENT`: пошук кав'ярень, збереження карток, перегляд історії.
  * `BARISTA`: керування своїми кав'ярнями, сканування клієнтських карток.

* **QR-коди та підписи**

  * Картки клієнтів вміщують зашифровані дані та підпис клієнта.
  * BARISTA сканує QR-код, перевіряє інформацію про заклад (назва, адреса тощо).
  * Сформовує власний підпис і відправляє на сервер обидва підписи.
  * Сервер перевіряє їх, тому що має публічні ключі обох сторін.

* **Локальне збереження через Room**

  * Картки, історія, статуси кав зберігаються на пристрої.

---

## 🔁 Обмін даними з сервером

Додаток використовує **REST API через Retrofit** для взаємодії з сервером. Дані передаються у форматі **JSON** за допомогою бібліотеки **Gson**.

**Технології:**

* Retrofit — HTTP клієнт
* GsonConverterFactory — для конвертації JSON ↔ Java

**Приклад:**

```java
    @POST("/api/auth/register")
Call<RegisterResponse> registerUser(@Body RegisterRequest request);

@POST("/api/auth/authorization")
Call<AuthorizationResponse> getAuthorization(@Body AuthorizationRequest request);

@POST("/api/auth/public-key")
Call<PublicKeyResponse> getPublicKey(@Body PublicKeyRequest request);

@POST("/api/auth/local-public-key")
Call<LocalPublicKeyResponse> setLocalPublicKey(@Body LocalPublicKeyRequest request);

@Multipart
@POST("/api/files/upload")
Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);


@GET("/api/files/download/{fileName}")
Call<ResponseBody> downloadFile(@Path("fileName") String fileName);

@POST("/api/request/search")
Call<SearchResponse> search(@Body SearchRequest request);
```

* `RetrofitClient` формує базовий URL (`http://192.168.177.3:8080`).
* Всі запити відправляються через клас `ApiHelper`, який обробляє відповіді та помилки через колбеки (`ApiCallback`).
* Реєстрація виконується через `AuthManager.registration(...)`, що інкапсулює всю логіку виклику.

---

## 📂 Репозиторії

* Клієнт: [github.com/rifatismailov/Coffee-Mark](https://github.com/rifatismailov/Coffee-Mark)
* Сервер авторизації та ключів: [github.com/rifatismailov/coffee\_mark\_server](https://github.com/rifatismailov/coffee_mark_server)

---

## 🚀 У майбутньому

* Шифрування зображень перед відправкою
* Синхронізація карток між девайсами
* Обмін повідомленнями залішається в планах

---

> Розроблено різнорівневою інфраструктурою для комфортної взаємодії між клієнтом та кав'ярнею.

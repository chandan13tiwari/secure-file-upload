# secure-file-upload

This application shows an example to how we can upload any file to Amazon S3 and download it from S3. We are uploaded an
encrypted file that means before uploading file to S3 we are encrypting the file using AES key and further secure that 
AES key with RSA encryption algorithm and done same for
downloading. We are downloaded the encrypted file and then decrypted it using that encrypted key.

#### Libraries used

* SpringBoot
* Thymeleaf
* Lombok
* Apache Commons IO
* Amazon AWS SDK
* SpringBoot JPA
* postreSQL 

#### Details you need to update according to your need before executing your application

* Add your AWS Access key,Secret key and aws region in application.properties file
* Create a folder called "file" under /resources folder to store the downloaded files
* Update your unique secret key which used in encryption/decryption in "Constants.class"



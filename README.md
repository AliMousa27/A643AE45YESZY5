<div align="center">


<img src="https://cdn.pixabay.com/photo/2016/06/13/13/46/shark-1454245_1280.png" width="200" />


</div>


<div align="center">
<img alt="Static Badge" src="https://img.shields.io/badge/Middleware_CI-passed-green">
<img alt="Static Badge" src="https://img.shields.io/badge/Frontend_CI-passed-green">
<img alt="Static Badge" src="https://img.shields.io/badge/Backend_CI-passed-green">
</div>
<div align="center">
        <img width ="40" alt="java img" src="https://cdn.freebiesupply.com/logos/large/2x/java-14-logo-png-transparent.png">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <img width ="40" alt="javascript img" src="https://static.vecteezy.com/system/resources/previews/027/127/463/original/javascript-logo-javascript-icon-transparent-free-png.png">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <img width ="40" alt="flutter img" src="https://storage.googleapis.com/cms-storage-bucket/0dbfcc7a59cd1cf16282.png">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <img width ="40" alt="Node JS" src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Node.js_logo.svg/590px-Node.js_logo.svg.png">
</div>


  # ToothTrek
</div>


**Table of Contents:**

- [ToothTrek](#toothtrek)
  * [Purpose](#purpose)
  * [Benefits](#benefits)
  * [Setup](#setup)
  * [Dentists API integration](#dentists-api-integration)
  * [Architecture](#architecture)


## Purpose


ToothTrek simplifies dental appointments in Sweden, specifically Gothenburg. Our application will enable easy booking for patients and dentists alike. Patients can find available time slots and dental offices, while dentists provide their schedules.


## Benefits




* Platform Compatibility 🔄


  * The application is available on Android, IOS, web browsers, and as a desktop application


* Load tolerance ⚙️
  * The system can handle 100.000 requests simultaneously


* Intuitive interface 🧩
  * The website is easily navigable by anyone, where making an appointment is but a click away!


* Interactive map 🗺️
  * Interactive map that visualizes the locations of the offices


* Scalable system 📈
  * Adding new services in future updates is independent of other components


## Setup
The system consists of 4 main components, the website/app frontend, the express backend server, the dentist API, and the various services in the middleware layer. All four components must be set up correctly for the system to function properly.


**Refer to the various parts of the system to set them up**


* [Flutter frontend setup](https://github.com/AliMousa27/Toothtrek/tree/main/Website/Frontend/README.md)
* [Express backend setup](https://github.com/AliMousa27/Toothtrek/tree/main/Website/Backend/README.md)


* *MQTT and middleware services setup*
  * [Logs Service](https://github.com/AliMousa27/Toothtrek/tree/main/Middleware/Logs/README.md)
  * [Bookings Service](https://github.com/AliMousa27/Toothtrek/tree/main/Middleware/bookings/README.md)
  * [Dental Records Service](https://github.com/AliMousa27/Toothtrek/tree/main/Middleware/dentalRecord/README.md)
  * [Notifications Service](https://github.com/AliMousa27/Toothtrek/tree/main/Middleware/notifications/README.md)


**Note: You need all the middleware services running for the system to function properly**


## Dentists API integration
Our system sports a comprehensive API that any external dentist can utilize to manage their patients bookings. In order to use our API, you will have to create a client and create requests to the appropriate endpoints in order to integrate and register in our system. However if your hosting our system, refer to the [Dentist API setup instructions](https://github.com/AliMousa27/Toothtrek/blob/main/DentistUI/README.md)


## Architecture
Our architecture is a service oriented architecture.
<div align="center">
<img src = "https://github.com/AliMousa27/Toothtrek/wiki/uploads/ca6c5e7b9c24f6ab56e04f0411428b00/image.png"></img>
</div>

For more detailed information, refer to the [architecture wiki page](https://github.com/AliMousa27/Toothtrek/wiki/architecture)

For a thorough analysis of how the architecture performs, refer to our [system analysis wiki page](https://github.com/AliMousa27/Toothtrek/wiki/system-analysis)  


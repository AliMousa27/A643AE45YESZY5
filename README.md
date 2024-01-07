
<div align="center">

<img src="https://cdn.pixabay.com/photo/2016/06/13/13/46/shark-1454245_1280.png" width="200" />

</div>

<div align="center">
![Middleware tests](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek//badges/main/pipeline.svg?job=androidBuild&key_text=Middleware+CI&key_width=80)
![Frontend CI](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek//badges/main/pipeline.svg?job=androidBuild&key_text=Frontend+CI&key_width=80)
![Backend CI](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek//badges/main/pipeline.svg?job=androidBuild&key_text=Backend+CI&key_width=80)
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

[[_TOC_]]

## Purpose

ToothTrek simplifies dental appointments in Sweden, specifically Gothenburg. Our application will enable easy booking for patients and dentists alike. Patients can find available time slots and dental offices, while dentists provide their schedules. 

## Benefits


* Platform Compatibility 🔄

  * The application is available on Android, IOS, web browsers, and as a desktop application

* Load tolerance ⚙️
  * The system can handle 100.000 requests simultanously 

* Intuitive interface 🧩
  * The website is easily navigable by anyone, where making an apointmnet is but a click away!

* Interactive map 🗺️
  * Interactive map that visualizes the locations of the offices

* Scalable system 📈
  * Adding new services in future updates is independant of other components

## Setup
The system consists of 4 main components, the website/app frontend, the express backend server,the dentist API, and the various services in the middleware layer. All four components must be set up correctly for the system to function properly.

**Refer to the various parts of the system to set them up**

* [Flutter frontend setup](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Website/Frontend/README.md?ref_type=heads)
* [Express backend setup](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Website/Backend/README.md?ref_type=heads)

* *MQTT and middleware services setup*
  * [Logs Service](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Middleware/Logs/README.md?ref_type=heads)

  * [Bookings Service](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Middleware/bookings/README.md?ref_type=heads)
  * [Dental Records Service](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Middleware/dentalRecord/README.md?ref_type=heads) 
  * [Notifications Service](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/Middleware/notifications/README.md?ref_type=heads)

**Note: You need all the middleware services running for the system to function properly** 

## Dentists API integration
Our system sports a comprehensive API that any exterenal dentist can utilize to manage their patients bookings. In order to use our API, you will have to create a client and create requests to the appropriate end points in order to integerate and register in our system. However if your hosting our system, refer to the [Dentist API setup instructions](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/blob/main/DentistUI/README.md?ref_type=heads)

## Architecture
Our architecture is a service oriented architecture.
<div align="center">
![Architecture diagram](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/wikis/uploads/ca6c5e7b9c24f6ab56e04f0411428b00/image.png)
</div>
For more detailed information, refer to the [architecture wiki page](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-12/toothtrek/-/wikis/Architecture/edit)

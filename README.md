# Spring boot Reactive - DEMO

Spring boot reactive application that uses reactive pipelines in every level from db until the controller
___

- Controller implemented using router functions
- Setup class fetches json payload from third party api and parse Json Response using reactive pipelines and save objects using reactive repository

## TODO

- [x] implement Setup class that fetch data from downstream 
  - [x] parse Json reactively 
  - [x] Save parsed data reactively into repository
- [x] implement Controller using Router Functions
  - [x] implement GET - get All 
  - [x] implement POST - add ONE
  - [x] implement search by name ?q={name}
  - [x] implement DELETE by id

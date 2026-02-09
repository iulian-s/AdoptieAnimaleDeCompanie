---
title: My App
emoji: 🏢
colorFrom: pink
colorTo: indigo
sdk: docker
app_port: 7860
pinned: false
short_description: Pet adoption platform
---

Proiect de licenta cu tema "Monitorizarea animalelor fara stapan", ce consta intr-o aplicatie android care permite utilizatorilor sa vada si sa adauge anunturi despre animalute date spre adoptie.

Prin tehnologiile utilizate si resursele utilizate se numara:
- Backend RESTful API Spring Boot 3.5.7 cu kotlin (JPA/WEB/Security/Validation/Dotenv/Actuator)
- Frontend React Vite pentru rolul de administrator
- Teste automate (JUnit5, Mockito)
- Baza de date PostgreSQL in cloud pe Supabase
- Director pentru incarcarea imaginilor din anunturi de tip bucket AWS S3 pe Supabase
- JWT pentru autentificare/definire roluri
- Jsoup pentru protectie html
- Model de invatare automata pentru verificarea imaginilor tip ONNX - https://huggingface.co/jdp8/nsfw_image_detection
- Rate limiter pentru endpointuri POST
- Baza de date pentru localitatile din Romania - https://github.com/catalin87/baza-de-date-localitati-romania
- Deployment Backend folosind Docker pe HuggingFace si Github Actions Workflow pentru automatizare
- Aplicatie Android folosind JetpackCompose, care apeleaza endpointurile de pe serverul backend de pe HuggingFace - https://github.com/iulian-s/AdoptieAndroid

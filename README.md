# ExpressTalk
ExpressTalk is a real-time communication platform designed to facilitate efficient conversations between users
The project is built using different technologies:
> Backend
* Java Spring
* PostgreSQL
* JUnit
* Mockito
> Frontend
* React
* Material UI
* Material UI Icons
* axios
* react-hook-form
* Redux Toolkit
* react-redux
* redux-persist
* react-router-dom
* Stompjs
* generate-react-cli
* libphonenumber-js
* sass
* sockjs-client
* typescript
* yup

## Features 
**User Authentication Form:**
Secure user login and registration for a personalized experience.

**Real-Time Communication:**
Enjoy seamless real-time messaging in both private and group chats.

**Create Groups & Private Chats:**
Easily initiate private conversations or form new groups with multiple users.

**Profile Customization:**
Update your avatar, name, and login details to personalize your profile.

**System Messages:**
Get notified of important system updates and chat events.

**Send Images in Messages:**
Share images directly within chat conversations.

**Group Management:**
Create and manage groups, including editing group settings and chat details.

**Role Assignment:**
Assign roles like admin and member to group participants.

**Member Management:**
Add or remove members from groups with ease.

**Chat Search:**
Quickly find chats with a built-in search feature.

**Member List:**
View a complete list of members in each group.

**Log Out:**
Securely log out of your account when needed.

## How to launch:
1. Clone the repository:
```bash
git clone https://github.com/PromisingDevelopment/ExpressTalk.git
```
2. Navigate to root project folder:
```bash
cd ExpressTalk
```
3. Open Docker desktop
4. Run backend and frontend successively:
```bash
cd ExpressTalk/
cd backend/
docker-compose up -d

cd ..
cd frontend/
yarn
yarn start

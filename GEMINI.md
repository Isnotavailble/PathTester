# Gemini Project Guidelines

Welcome to the **Path Tester Monorepo Project**. This document serves as the guide for LLM agents (like Gemini) when navigating, reading, writing, and editing code within this repository.

---

## 🚫 Gitignore Rules & Access Permissions

To prevent unwanted changes and protect workspace performance, you must respect the ignore configurations. **Do NOT read or write files or directories** that match any patterns in the project's `.gitignore` files.

### Frontend Ignored Patterns (from [frontend/.gitignore](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/frontend/.gitignore))
- `node_modules/`
- `dist/` and `dist-ssr/`
- Local environment files (e.g., `*.local`)
- IDE config directories (e.g., `.vscode/`, `.idea/`, `.DS_Store`, `*.suo`, etc.)
- Logs (e.g., `logs`, `*.log`, `npm-debug.log*`, etc.)

### Backend Ignored Patterns (from [backend/.gitignore](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/backend/.gitignore))
- Build output directories (e.g., `target/`, `build/`, `/dist/`, `/nbdist/`, `/nbbuild/`)
- IDE/Editor files (e.g., `.idea/`, `*.iws`, `*.iml`, `*.ipr`, `/nbproject/private/`, `.vscode/`, `.settings/`, `.project`, `.classpath`, `.factorypath`, `.springBeans`, `.sts4-cache`, `.apt_generated`)
- Maven wrapper JAR (e.g., `.mvn/wrapper/maven-wrapper.jar`)

---

## 📁 Repository Folder Structure

Below is the directory layout of the non-ignored project source code:

```
pathtester_project/
├── backend/                                                     # Java Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/isnotavailable/me/pathtester/
│   │   │   │   ├── PathtesterApplication.java                   # Main Spring Boot Application Entry
│   │   │   │   ├── controllers/                                 # API Controllers Layer
│   │   │   │   │   └── MainController.java
│   │   │   │   ├── dtos/                                        # Data Transfer Objects
│   │   │   │   │   ├── AstarRequest.java
│   │   │   │   │   ├── AstarResult.java
│   │   │   │   │   ├── BfsRequest.java
│   │   │   │   │   ├── BfsResult.java
│   │   │   │   │   ├── DfsRequest.java
│   │   │   │   │   ├── DfsResult.java
│   │   │   │   │   ├── Node.java
│   │   │   │   │   └── Point.java
│   │   │   │   ├── postconstructs/                              # Application Startup logic
│   │   │   │   └── services/                                    # Business Logic Layer
│   │   │   │       ├── AstarService.java
│   │   │   │       ├── BfsService.java
│   │   │   │       ├── DfsService.java
│   │   │   │       └── GraphGeneratorService.java
│   │   │   └── resources/                                       # Configuration & Static Assets
│   │   │       ├── application.properties
│   │   │       ├── static/
│   │   │       └── templates/
│   │   └── test/
│   │       └── java/com/isnotavailable/me/pathtester/
│   │           ├── PathtesterApplicationTests.java
│   │           └── services/                                    # Service Layer Unit Tests
│   │               ├── AstarServiceTest.java
│   │               ├── BfsServiceTest.java
│   │               └── DfsServiceTest.java
│   ├── Dockerfile                                               # Dockerfile for Render/Docker Deployment
│   ├── pom.xml                                                  # Maven Configuration
│   └── mvnw / mvnw.cmd
│
└── frontend/                                                    # React + Vite Frontend
    ├── src/
    │   ├── api/                                                 # API Service Clients
    │   │   ├── astarService.js
    │   │   ├── bfsService.js
    │   │   └── dfsService.js
    │   ├── assets/                                              # Static Assets
    │   │   ├── hero.png
    │   │   ├── react.svg
    │   │   └── vite.svg
    │   ├── components/                                          # UI Components
    │   │   ├── Alert/
    │   │   │   ├── Alert.jsx
    │   │   │   └── Alert.module.css
    │   │   ├── ControlPanel/
    │   │   │   ├── ControlPanel.jsx
    │   │   │   └── ControlPanel.module.css
    │   │   ├── Header/
    │   │   │   ├── Header.jsx
    │   │   │   └── Header.module.css
    │   │   ├── Legend/
    │   │   │   ├── Legend.jsx
    │   │   │   └── Legend.module.css
    │   │   ├── MatrixGrid/
    │   │   │   ├── MatrixGrid.jsx
    │   │   │   └── MatrixGrid.module.css
    │   │   └── StatsPanel/
    │   │       ├── StatsPanel.jsx
    │   │       └── StatsPanel.module.css
    │   ├── App.css
    │   ├── App.jsx                                              # Main React Application Component
    │   ├── index.css                                            # Global CSS
    │   └── main.jsx                                             # React DOM Entrypoint
    ├── index.html
    ├── package.json
    ├── vite.config.js
    └── eslint.config.js
```

---

## 🎨 Frontend Architecture Rules

To maintain separation of concerns and component isolation in the frontend:
1. **Component Folder Isolation**: Every single React component must reside within its own dedicated sub-folder under [components](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/frontend/src/components).
2. **File Separation**: The component folder must **only** contain:
   - A `.jsx` file containing the rendering and logic (e.g., [Alert.jsx](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/frontend/src/components/Alert/Alert.jsx)).
   - A `.module.css` file containing styles scoped only to that component (e.g., [Alert.module.css](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/frontend/src/components/Alert/Alert.module.css)).
3. Do not place global utility files, other React components, or style sheets directly in the component's folder.

---

## ⚙️ Backend Architecture Rules

To maintain clean and standard Spring Boot practices in the backend:
1. **Package by Layer**: Organise classes strictly into packages according to their architecture layer, specifically:
   - [controllers](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/backend/src/main/java/com/isnotavailable/me/pathtester/controllers)
   - [dtos](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/backend/src/main/java/com/isnotavailable/me/pathtester/dtos)
   - [services](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/backend/src/main/java/com/isnotavailable/me/pathtester/services)
   - [postconstructs](file:///c:/Users/AnyaWalker/Desktop/GeminiPlayGround/pathtester_project/backend/src/main/java/com/isnotavailable/me/pathtester/postconstructs)
2. **Readable & Strict Naming Conventions**: Service implementations must follow clean, readable naming rules.
   - Every service class must be named using the `[serviceName]Service` pattern (e.g., `AstarService`, `BfsService`, `DfsService`, `GraphGeneratorService`).
   - Layer suffixes should be clear and explicitly used to specify the role of the class (e.g., controllers suffixed with `Controller`, DTOs with `Request` or `Result` / `Dto`).

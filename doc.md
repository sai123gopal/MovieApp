# AI Agent Assessment - Technical Documentation

## 📌 Overview

Build an intelligent AI agent that processes and presents data retrieved from a third-party API (TMDB). The agent should support user interaction and allow exploration of data with useful features such as detail views, local data persistence, and offline handling.

---

## ✅ Requirements

### 🔍 Core Features
- **Data Listing**: Fetch and display trending items (e.g., movies) using the appropriate API endpoints.
- **Detail View Navigation**: Navigate to a screen with detailed information for a selected item.
- **Favorites**: Enable marking items as favorites and persist them across sessions.
- **Caching**: Cache fetched data locally to improve performance and reduce redundant API calls.
- **Error Handling**: Gracefully handle network or API errors and display appropriate feedback to users.

---

## 🛠️ Technology Guidelines

### Android (Kotlin)
- Use **Kotlin** and **Jetpack Compose** for building the UI.
- Follow modern Android architecture practices.
- Ensure smooth state handling and lifecycle awareness.

---

## 🧱 Software Architecture

- Apply **SOLID principles**.
- Design a clean, maintainable architecture.

---

## 🔐 API Key Handling

- Never hardcode your API key in the codebase.
- Use environment-specific configurations or secure local storage solutions.

---

## 🖼️ Image Handling (if using TMDB)

- Use the `poster_path` field from the API response.
- Prepend with the TMDB base image URL (see: [Image Basics Guide](https://developer.themoviedb.org/docs/image-basics)).

---

### Required
- The solution must compile successfully.
- No crashes, bugs, or unresolved warnings.
- Follows SOLID principles and good architectural practices.
- Code is clean, modular, and easy to understand.
- Git history is clear, organized, and traceable.

---

## 💡 Hints

- Keep the implementation simple and focused.
- Be clear and concise in both code and documentation.

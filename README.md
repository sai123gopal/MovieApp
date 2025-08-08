# 🎬 MovieHub

A modern **Android application** built with **Jetpack Compose** that showcases **popular** and **trending movies** using the [TMDB API](https://www.themoviedb.org/).

---

## 📋 Prerequisites

- **Android Studio**: Koala (2024.2.1)
- **Android SDK**: 34
- **Kotlin**: 1.9.0+
- **Gradle**: 8.0+
- **TMDB API Key**

---

## ⚙️ Setup

### 1️⃣ Get Your TMDB API Key
1. Visit the [TMDB API](https://www.themoviedb.org/settings/api) page.
2. Sign up / log in to your TMDB account.
3. Request an **API Key**.
4. Create a `local.properties` file in your **project root** (if it doesn’t exist).
5. Add your API key:

```properties
TMDB_API_KEY=your_api_key_here
```

### 2️⃣ Sync the Project
- In Android Studio, click **Sync Project with Gradle Files** to apply the API key.

---

## 🚀 Run the App

1. Open the project in Android Studio (Koala).
2. Make sure Android SDK 34 is installed and selected.
3. Connect an Android device or start an emulator.
4. Build and run the app.

---

## 📱 Screenshots

| **Home Screen** | **Trending Movies** | **Movie Details** |
|-----------------|---------------------|-------------------|
| <img src="https://github.com/user-attachments/assets/634ef1c8-b910-4a14-bf5e-f9363c148d19" width="250" alt="Home Screen"> | <img src="https://github.com/user-attachments/assets/92a588de-a92b-4961-b810-65ee71c973ec" width="250" alt="Trending Movies"> | <img src="https://github.com/user-attachments/assets/aad115ee-3f57-405c-b03b-8442288c8592" width="250" alt="Movie Details"> |

---

## ✅ Features

- Modern UI built with **Jetpack Compose**
- Displays **Popular**, **Trending**, and **Top Rated** movies
- Movie details screen with poster, overview, release date, rating, and genres
- Clean architecture ready for extension

---


## ✅ Recommended Libraries

- Retrofit (Networking)
- OkHttp (HTTP client)
- Coil (Image loading)
- Hilt / Dagger (Dependency injection)
- Kotlin Coroutines / Flow (Asynchronous)
- Jetpack Compose (UI)

---

## 🔒 Security

- **Never** commit `local.properties` with your TMDB API key to version control. Use `.gitignore` to exclude it.

---

## 📄 License

This project is licensed under the MIT License. Feel free to use and modify it.

---

Made with ❤️ — happy coding!

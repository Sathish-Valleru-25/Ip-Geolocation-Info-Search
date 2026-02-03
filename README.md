**IP Geolocation Search**

This project fetches IP geolocation data using the IP-API and stores it in a local database for future retrieval. The app fetches data either from the local database or an API, depending on the availability of data, and uses MVVM and Clean Architecture for organizing the code.

**Tech Stack**

Kotlin: The primary programming language used for developing the Android app.

MVVM (Model-View-ViewModel): Architecture pattern for organizing UI code in a clean and modular way.

Clean Architecture: Ensures separation of concerns and makes the app scalable, testable, and maintainable.

Hilt: Dependency Injection framework for managing dependencies in a clean and scalable way.

Retrofit: A type-safe HTTP client for Android to interact with the IP-API for fetching geolocation data.

Room: Android's database library for storing geolocation data locally.

**Features**

IP Address Search: The user can input an IP address, and the app will retrieve the geolocation data for that IP.

Database Caching: If the geolocation data for the IP address is already stored in the local database, it is retrieved. Otherwise, the data is fetched from the API and stored in the database.

Automatic Data Refresh: The app checks if the stored data is older than 5 minutes and refreshes the data if needed.

Error Handling: The app gracefully handles API failures, input validation errors, and logs issues for better debugging.

Logging: Provides basic logging for debugging purposes during development.

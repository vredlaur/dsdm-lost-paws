# LostPaws

LostPaws este o aplicatie Android pentru proiectul final DSDM, tema Social / Lost Pets. Aplicatia ajuta utilizatorii sa raporteze si sa urmareasca animale pierdute sau gasite.

## Functionalitati

- Autentificare si inregistrare locala, fara Firebase si fara backend extern.
- Sesiune persistenta cu SharedPreferences.
- Anunturi locale pentru animale pierdute/gasite salvate in Room.
- Lista scrollabila cu filtre pentru Toate, Pierdute si Gasite.
- Cautare dupa nume, oras, zona, rasa, tip sau status.
- Ecran de detalii cu imagine, descriere, contact, recompensa, favorit, rezolvat si stergere pentru owner.
- Apel direct catre numarul de contact si distribuire anunt prin share sheet Android.
- Adaugare anunt local cu validare.
- Ecran cu imagini online incarcate prin doua request-uri HTTP: Dog CEO API si The Cat API.
- Profil cu statistici locale si delogare.
- Stari de loading, empty si error pentru scenarii fara date sau fara internet.

## Tehnologii

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Room Database
- SharedPreferences
- Retrofit + Gson
- OkHttp logging interceptor
- Coil Compose
- ViewModel + Coroutines + Flow

## Cerinte DSDM bifate

| Cerinta | Implementare |
| --- | --- |
| Git / GitHub | Repo: `vredlaur/dsdm-lost-paws` |
| Activity | `MainActivity` cu `setContent` |
| Screens / Navigation | `NavController`, `NavHost`, rute Compose |
| Login / Register | Local, cu utilizatori in Room |
| Persistenta locala | Room: `users`, `pet_announcements` |
| DAO / Database / Repository | `UserDao`, `PetDao`, `AppDatabase`, repository-uri |
| SharedPreferences | `SessionManager` pentru sesiune si filtru |
| Lista scrollabila | `LazyColumn` cu `PetCard` |
| Minimum 2 HTTP requests | Dog CEO API + The Cat API |
| Drawables XML | `lost_paws_card_shape`, `lost_paws_button_selector`, `lost_paws_header_gradient` |
| Color selector | `lost_paws_text_selector` |
| No crash | Empty/error states, validari si fallback pentru imagini |

## Ecrane

- Login
- Register
- Home
- Add Pet
- Pet Details
- Remote Pets
- Profile

## API-uri HTTP

- Dog CEO API: `https://dog.ceo/api/breeds/image/random`
- The Cat API: `https://api.thecatapi.com/v1/images/search?limit=10`

## Baza de date locala

Room este folosit ca abstractie peste SQLite. Aplicatia are doua tabele:

- `users`: conturi locale pentru autentificare.
- `pet_announcements`: anunturi locale cu animale pierdute sau gasite.

La prima pornire, daca nu exista anunturi, aplicatia adauga patru exemple: Bella, Mimi, Rex si Luna. Seed-ul nu se repeta la fiecare lansare.

## SharedPreferences

`SessionManager` salveaza:

- starea de login
- id-ul utilizatorului
- email-ul
- numele complet
- filtrul selectat pe Home

SharedPreferences este folosit intentionat pentru cerinta de laborator privind valori simple key-value.

## Build

Pe Windows:

```powershell
.\gradlew.bat build
```

Proiectul foloseste Android SDK 36.1 si Android Gradle Plugin 8.13.2.

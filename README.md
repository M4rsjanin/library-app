# Library Management REST API

## Opis projektu
Aplikacja webowa typu REST API stworzona w technologii Spring Boot, służąca do zarządzania biblioteką. System umożliwia zarządzanie katalogiem książek, użytkownikami (kartami bibliotecznymi) oraz procesem wypożyczania i zwrotów. 

Projekt został zrealizowany ze szczególnym naciskiem na bezpośrednie użycie specyfikacji JPA oraz narzędzia Hibernate (za pośrednictwem klasy `EntityManager`), celowo wykluczając interfejsy Spring Data JPA. Podejście to gwarantuje pełną kontrolę nad generowanymi zapytaniami do bazy danych oraz optymalizację operacji takich jak stronicowanie wyników.

## Stack technologiczny
* Język: Java 25
* Framework: Spring Boot 4.0.3 (Web, Validation)
* Baza danych: H2 (In-Memory)
* ORM: Hibernate / JPA (wyłącznie przez `EntityManager`)
* Testowanie: JUnit 5, Mockito
* Narzędzie budowania: Maven

## Kluczowe funkcjonalności
* Brak Spring Data JPA: Warstwa dostępu do danych (Repository) zaimplementowana od zera przy użyciu wstrzykiwanego kontekstu persystencji (`EntityManager`) i zapytań JPQL.
* Własny mechanizm Cache: Niestandardowy, autorski system pamięci podręcznej (z czasem wygasania ustalanym na 5 minut) optymalizujący pobieranie list wypożyczonych książek, oparty na strukturach współbieżnych (`ConcurrentHashMap`).
* Paginacja: Ręcznie zaimplementowane stronicowanie wyników po stronie bazy danych (`setFirstResult`, `setMaxResults`).
* Globalna obsługa wyjątków: Domenowe klasy wyjątków (np. `UserNotFoundException`, `BookNotBorrowedException`) obsługiwane za pomocą mechanizmu `@ControllerAdvice`, zwracające precyzyjne kody statusów HTTP (400, 404).
* Walidacja danych wejściowych: Zabezpieczenie kontrolerów przy pomocy specyfikacji `jakarta.validation` (`@NotBlank`, `@Valid`), gwarantujące integralność danych przed procesowaniem logiki biznesowej.

## Instrukcja uruchomienia
Aplikacja wykorzystuje wbudowaną bazę danych H2 w pamięci operacyjnej, co eliminuje konieczność zewnętrznej konfiguracji środowiska bazodanowego.

1. Pobierz repozytorium na dysk lokalny.
2. W głównym katalogu projektu uruchom wiersz poleceń i wykonaj instrukcję:
   - Środowisko Windows: `mvnw spring-boot:run`
   - Środowisko Linux/macOS: `./mvnw spring-boot:run`
3. Aplikacja zostanie uruchomiona na domyślnym porcie `8080`.

## Dokumentacja Endpointów API

### Zarządzanie katalogiem książek
* GET `/api/books?page=0&size=10` 
  Zwraca stronicowaną listę książek wraz z informacją o użytkownikach aktualnie je wypożyczających.
* POST `/api/books` 
  Rejestruje nową pozycję w katalogu bibliotecznym. Wymaga poprawnego obiektu JSON zgodnego z kontraktem DTO.

### Zarządzanie użytkownikami i wypożyczeniami
* GET `/api/users` 
  Zwraca listę wszystkich zarejestrowanych użytkowników.
* GET `/api/users/{cardId}/books` 
  Zwraca listę tytułów aktualnie wypożyczonych przez użytkownika o zadanym identyfikatorze (Endpoint optymalizowany mechanizmem Cache).
* PATCH `/api/users/{cardId}/lastname?name={newLastName}` 
  Aktualizuje nazwisko wskazanego użytkownika.
* POST `/api/users/{cardId}/borrow/{bookId}` 
  Rejestruje proces wypożyczenia książki przez użytkownika.
* DELETE `/api/users/{cardId}/borrow/{bookId}` 
  Odnotowuje zwrot książki, zwalniając blokadę wypożyczenia.

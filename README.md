# LeetCode Curriculum Tracker

A self-hosted Spring Boot application for organising LeetCode practice into ordered
curricula and modules, logging submissions, and keeping notes per problem.

Built to learn Java, Spring Boot, and server-rendered MVC by implementing a domain with
real structure — ordered many-to-many relationships, external API ingestion, a layered
service architecture — rather than a tutorial CRUD app. LeetCode's own lists cover some
of this; the point was the implementation.

## Stack

- **Java 25**, **Spring Boot 4.1** (Web MVC, Data JPA, Actuator)
- **PostgreSQL** via Hibernate
- **Thymeleaf** + Bootstrap 5 for server-rendered views
- **jsoup** for HTML scraping, LeetCode's GraphQL API for problem metadata
- Gradle, JUnit 5, AssertJ

## Quickstart

Requires a JDK 25 toolchain and a running PostgreSQL instance.

```bash
# 1. Create the database
createdb leetcodetracker

# 2. Configure credentials
cp .env.example .env
$EDITOR .env

# 3. Run (sources .env, then ./gradlew bootRun)
./run.sh        # run.cmd on Windows
```

The app serves on `http://localhost:8080`. The schema is created automatically on first
start (`ddl-auto: update`), so no migration step is needed yet — see
[Known limitations](#known-limitations).

`dump.sh` and `restore.sh` wrap `pg_dump`/`pg_restore` for snapshotting your data.

## Usage

1. Create a **curriculum** — a practice plan, e.g. Grind 75.
2. Add **modules** to it. A module is a sub-grouping, typically a week or a topic.
3. Import **problems** from LeetCode by slug, or scrape a NeetCode list, then associate
   them with curricula and modules. Ordering within each is explicit and reorderable.
4. Log a **submission** when you finish a problem: date, elapsed time, language, and notes.

Imports are explicit — nothing is pulled from your LeetCode account, and no scraping runs
on a schedule.

## Architecture

Packages are organised **by feature** rather than by layer — `curriculum/`, `modules/`,
`problem/`, `leetcode/`, `importer/` — each holding its own entities, repositories,
service, controllers, and domain exceptions.

A few things worth pointing at:

- **Dual controllers over a shared service layer.** Each feature exposes a REST
  `@RestController` and a Thymeleaf `@Controller`, both delegating to the same service, so
  no business logic is coupled to a transport.
- **Explicit join entities.** `CurriculumProblem` and `ModuleProblem` are real entities
  carrying a `position`, rather than a bare `@ManyToMany`, because association order is
  part of the domain.
- **[`Ordering<T extends Positionable>`](src/main/java/com/example/leetcodetracker/ordering/Ordering.java).**
  Insert, move, remove, and renumber logic for ordered collections, extracted once and
  reused across curriculum modules, curriculum problems, and module problems. Maintains
  the contiguous zero-based `position` invariant that `@OrderBy("position ASC")` depends
  on. Unit tested in
  [`OrderingTest`](src/test/java/com/example/leetcodetracker/ordering/OrderingTest.java).
- **Two ingestion paths.** A GraphQL client against LeetCode's API for problem metadata,
  and a jsoup scraper for NeetCode list structure.

### Development note

The `Module` entity collides with `java.lang.Module`, so that import is load-bearing in
files touching both. `Chapter` would have been the better name; renaming it is on the list.

## Testing

```bash
./gradlew test --tests "*OrderingTest*"   # unit tests, no database required
./gradlew test                            # full suite; needs Postgres + .env for the context test
```

## Scope and non-goals

Deliberately out of scope, since this is a single-user local tool:

- **No authentication.** Don't expose it to a network you don't control.
- **No LeetCode account integration.** It does not read your submissions, and needs no
  credentials or premium subscription.
- **No automatic scraping.** Imports are user-initiated.
- **No deployment story.** Local Postgres and `bootRun`; no container or hosting config.

## Known limitations

- **Schema is managed by `ddl-auto: update`.** Fine for local use, wrong for anything else.
  Flyway migrations are the next structural change.
- **Test coverage is uneven.** `Ordering` is well covered; the service and controller
  layers are not yet.
- Notes and other user text are rendered through Thymeleaf's default escaping, but the app
  assumes a single trusted user and has had no security review.

## Roadmap

- Flyway migrations replacing `ddl-auto`
- `@WebMvcTest` and service-layer coverage
- Completion-time statistics by language, difficulty, and topic tag
- Drag-to-reorder in place of form-entry positions
- Filter problems by name; show tag and association counts on list views
- Rename `Module` to `Chapter`

## License

See [LICENSE](LICENSE).

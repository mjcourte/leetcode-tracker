# Learning Java/SpringBoot/MVC/monolith by making this dumb leet code tracker thing

all at once because im stupid

- the domain is still example, and i do not care to change it
- you import your own stuff, it doesnt autoscrape
- doesnt require any auth, doesnt use any auth, does not use your LC account at all
- you run your own local postgres and set .env
- you make your own curricula with modules and or problems.
- eg grind 75 has 8 weeks (8 modules), then you associate all the things
- when you finish a problem you can log the date, time elapse, language, and leave notes and write the solution for all i care
- the notes box is probably unsanitized so have fun with that
- then you leave it open in your browser forever while you do leetcode for the rest of your life

## TODO list

1. ~naviagate from problem up relationships to modules or curricula and module->curriculum~
2. ~filterable problems page (tag, difficulty)~
3. ~from a problem, clickable tag pills to problems page filtered by that tag~
4. ~problem import from somewhere webcontroller~
5. ~impl problem status~
6. ~problem status indicators everywhere a problem is listed. status "resettable" to indicate i want to try again~
7. aggregation of status for module/curricula eg "not started" "started" "complete" percent or X/Y, and date completed
8. graphs page which show completion time statistics per language/difficulty/topictag
9. ui dress up including any future ideas
10. draggable reorder move instead of form entry
11. ??
12. ~bulk importer page~
13. text-like file create data eg curriculum+modules+problems
14. refactor submissions to CollectionProblems?

## gripes

### curriculum detail page

- table does not need position index
- prefer move action to be drag than num entry + click

### module detail page

- check same as curriculum detail page

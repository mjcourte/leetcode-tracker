# Learning Java/SpringBoot

all at one because im stupid.

perhaps let make this a leetcode tracker lol

- they are callable via graphql
- for a userID/email
- design a curriculum
- tabulate the curriculum in sections (eg grind 75 week 7, or topic dynamic programming)
- pull question details, tags, status for account (completed in what lang)
- allow logging completion in language with time and notes
- review notes per topic/tag
- review statistics of time-to-finish problem by topic or difficulty
- gonna want an .env template that dictates env vars such as user ID / email
- but the actual copy of env is not tracked
- likewise init a db file that does not get tracked

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

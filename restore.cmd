@REM   this is NUCLEAR DELETE AND REPLACE

& "C:\Program Files\PostgreSQL\18\bin\dropdb.exe" -U postgres leetcodetracker

& "C:\Program Files\PostgreSQL\18\bin\createdb.exe" -U postgres leetcodetracker

& "C:\Program Files\PostgreSQL\18\bin\pg_restore.exe" `
    -U postgres `
    -d leetcodetracker `
    .\leetcodetracker.dump
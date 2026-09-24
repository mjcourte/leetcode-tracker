curl.exe -X POST http://localhost:8080/problems `
    -H "Content-Type: application/json" `
    --data-binary "@api/problems/create.json"
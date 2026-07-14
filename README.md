# LibraryManagementSystem
This is my first attempt to create a server that validates HTTP requests to create, read, update, and delete books from a SQLite database. The purpose of this project is to keep track of book descriptions and history. The end goal for the frontend of this project is for a user to be able to search for a book to see if it exists within the database. If it does exist, the user will have the option to check out a book that is available.

## Implementation
### Database
I first started by deciding how I would store the books. I quickly figured out I couldn't just make a book entity; I had to make 3 tables to store the book blueprint with its ISBN, title, and ID. I then needed to make a table for the physical copies of the books (properties included copy_id (ID for the specific book copy), book_id (the reference to the book blueprint), barcode, and checkout status) and make a table for the book authors (properties include ID (references a specific author), book_id, and author_name) since SQLite doesn't have arrays.

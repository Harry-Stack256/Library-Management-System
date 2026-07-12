# LibraryManagementSystem

This is my first attemept to create a server that vaildates http requests to create ,read ,update and delete books from a SQlite databases.
The purpose of this project is keep track of of book descriptions and history. The end goal for the frontend of this project is for a user to
able to search for  a book to see if it exsit within the database.If it does exsit the user will have the option to checkout a book that is
available.

## Implementation

### Database
I first started by deciding how I will store the books. I quickly firgured out I couldn't just make a book enity I had to make 3 tables to store the book blueprint 
with it's bn ,title and id. I then needed to make a table for the physical copies(properties included copy_id(id for the specfic book copy) ,book_id(the refrence to the book blueprint),
barcode and checkout status) of the books and make a table for the book authors(properties include id(refrences specfic author), book_id and author_name)
since Sqlite doesn't have arrays.

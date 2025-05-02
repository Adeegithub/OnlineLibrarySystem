The goal is to build an online library system where users can sign up, borrow books, and
track their borrowing history. The system will store user details, book information, and
borrowing records. The project involves designing the database schema, developing
REST APIs for user registration, borrowing transactions, and viewing history, as well as
optimizing the system's performance to efficiently manage data and ensure fast access to
resources.

Entities: <br>
● User (id, name, email, created_at) <br> 
● Book (id, title, author, published_year, available_copies <br>


Implementation: <br>
Implement REST APIs that allow users to interact with this system. You need to implement
the following basic functionalities: <br>

● A user should be able to create their profile by providing basic information. <br>

● Once registered, users should be able to explore and view the collection of books
and see which books have copies available to be borrowed. <br>

● The system should provide users with the ability to search for books based on
specific criteria. For example, users should be able to view the list of available
books by author and published year. <br>

● The user API should be secure against unauthorized access but any user will be
able to browse the available book collection in the library. <br>

Ensure that there is optimal performance and fast response times in the system as the
dataset grows, especially when handling large amounts of data. <br>

Add API endpoints to allow users to, <br>
● borrow and return available books. <br>
● see their past borrowed books. <br>

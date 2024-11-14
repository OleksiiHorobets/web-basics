from app.schemas import BookCreate, BookUpdate
from app.models import Book
from bson.objectid import ObjectId

def create_book(db, book: BookCreate):
    result = db.books.insert_one(book.dict())
    return str(result.inserted_id)

def get_book(db, book_id: str):
    data = db.books.find_one({"_id": ObjectId(book_id)})
    if data:
        return Book(id=str(data["_id"]), **data)
    return None

def get_books(db, skip: int = 0, limit: int = 10):
    books = []
    for book in db.books.find().skip(skip).limit(limit):
        books.append(Book(id=str(book["_id"]), **book))
    return books

def update_book(db, book_id: str, book: BookUpdate):
    update_data = {k: v for k, v in book.model_dump().items() if v is not None}
    if update_data:
        result = db.books.update_one({"_id": ObjectId(book_id)}, {"$set": update_data})
        if result.matched_count:
            return get_book(db, book_id)
    return None

def delete_book(db, book_id: str):
    result = db.books.delete_one({"_id": ObjectId(book_id)})
    return result.deleted_count > 0

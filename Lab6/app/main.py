from fastapi import FastAPI, HTTPException
from app.models import Book
from app.schemas import BookCreate, BookUpdate
from app.database import db
from app.crud import create_book, get_book, get_books, update_book, delete_book

app = FastAPI()

@app.post("/books/", response_model=Book)
async def create_book_endpoint(book: BookCreate):
    book_id = create_book(db, book)
    return get_book(db, book_id)

@app.get("/books/{book_id}", response_model=Book)
async def read_book(book_id: str):
    book = get_book(db, book_id)
    if book is None:
        raise HTTPException(status_code=404, detail="Book not found")
    return book

@app.get("/books/", response_model=list[Book])
async def read_books(skip: int = 0, limit: int = 10):
    books = get_books(db, skip=skip, limit=limit)
    return books

@app.put("/books/{book_id}", response_model=Book)
async def update_book_endpoint(book_id: str, book: BookUpdate):
    updated_book = update_book(db, book_id, book)
    if updated_book is None:
        raise HTTPException(status_code=404, detail="Book not found")
    return updated_book

@app.delete("/books/{book_id}")
async def delete_book_endpoint(book_id: str):
    result = delete_book(db, book_id)
    if not result:
        raise HTTPException(status_code=404, detail="Book not found")
    return {"detail": "Book deleted successfully"}

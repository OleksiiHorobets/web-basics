from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class BookCreate(BaseModel):
    title: str
    reader: str
    date_of_issue: datetime
    return_date: datetime

class BookUpdate(BaseModel):
    title: Optional[str] = None
    reader: Optional[str] = None
    date_of_issue: Optional[datetime] = None
    return_date: Optional[datetime] = None

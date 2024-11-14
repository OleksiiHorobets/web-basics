from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class Book(BaseModel):
    id: str
    title: str
    reader: str
    date_of_issue: datetime
    return_date: datetime

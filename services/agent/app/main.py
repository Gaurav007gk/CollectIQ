from datetime import date
from fastapi import FastAPI
from pydantic import BaseModel, Field
app=FastAPI(title="CollectIQ AI Agent",version="0.1.0")
class DraftRequest(BaseModel):
 customer_name:str=Field(max_length=180)
 invoice_number:str=Field(max_length=80)
 amount:str
 due_date:date
 days_overdue:int=Field(ge=0,le=3650)
@app.get("/health")
def health(): return {"status":"ok"}
@app.post("/draft")
def draft(x:DraftRequest):
 tone="gentle reminder" if x.days_overdue<=7 else "follow-up reminder"
 message=f"Hi {x.customer_name}, this is a {tone} for invoice {x.invoice_number} of ₹{x.amount}, due on {x.due_date}. Could you please share the expected payment date? If already paid, please disregard. Thank you."
 return {"draft":message,"message":message,"requires_approval":True}

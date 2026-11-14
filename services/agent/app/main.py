import os
from datetime import date
from fastapi import FastAPI
from pydantic import BaseModel,Field
import httpx
app=FastAPI(title="CollectIQ AI Agent",version="0.2.0")
class DraftRequest(BaseModel):
 customer_name:str=Field(max_length=180)
 invoice_number:str=Field(max_length=80)
 amount:str
 due_date:date
 days_overdue:int=Field(ge=0,le=3650)
@app.get("/health")
def health():return {"status":"ok","llm_configured":bool(os.getenv("OPENAI_API_KEY"))}
@app.post("/draft")
async def draft(x:DraftRequest):
 # OpenAI-compatible endpoint is optional. Deterministic safe fallback works offline.
 key=os.getenv("OPENAI_API_KEY");model=os.getenv("LLM_MODEL","gpt-4o-mini")
 if key:
  prompt=f"Write one concise, respectful Indian business payment reminder. No threats, no invented details. Customer {x.customer_name}; invoice {x.invoice_number}; amount INR {x.amount}; due {x.due_date}; days overdue {x.days_overdue}. Ask expected payment date and mention disregard if paid."
  try:
   async with httpx.AsyncClient(timeout=20) as client:
    r=await client.post("https://api.openai.com/v1/chat/completions",headers={"Authorization":f"Bearer {key}"},json={"model":model,"messages":[{"role":"system","content":"You draft professional payment reminders. Never claim a payment was made or threaten the customer."},{"role":"user","content":prompt}],"temperature":0.3})
    r.raise_for_status();message=r.json()["choices"][0]["message"]["content"].strip()
   return {"draft":message,"message":message,"requires_approval":True,"provider":"openai"}
  except Exception:
   pass
 tone="gentle reminder" if x.days_overdue<=7 else "follow-up reminder"
 message=f"Hi {x.customer_name}, this is a {tone} for invoice {x.invoice_number} of ₹{x.amount}, due on {x.due_date}. Could you please share the expected payment date? If already paid, please disregard. Thank you."
 return {"draft":message,"message":message,"requires_approval":True,"provider":"fallback"}

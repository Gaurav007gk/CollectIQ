from mcp.server.fastmcp import FastMCP
mcp=FastMCP("CollectIQ")
@mcp.tool()
def collection_summary()->dict:
 """Sample tenant collection summary (replace with authenticated API call)."""
 return {"outstanding_inr":277500,"overdue_inr":277500,"overdue_invoices":5,"tenant":"demo"}
@mcp.tool()
def draft_payment_reminder(customer_name:str,invoice_number:str,amount_inr:str,due_date:str)->str:
 """Draft a reminder only; never sends it."""
 return f"Hi {customer_name}, friendly reminder for invoice {invoice_number} of ₹{amount_inr}, due {due_date}. Please share expected payment date. Thank you."
if __name__=="__main__": mcp.run()

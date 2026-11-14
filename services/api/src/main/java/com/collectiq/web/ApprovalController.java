package com.collectiq.web;
import org.springframework.web.bind.annotation.*;import org.springframework.jdbc.core.JdbcTemplate;import java.util.*;
@RestController @RequestMapping("/api/approvals") public class ApprovalController{
 private final JdbcTemplate db;public ApprovalController(JdbcTemplate d){db=d;}
 @GetMapping public List<Map<String,Object>> pending(){return db.queryForList("select id,invoice_id,recipient,message,status,created_at from reminder_approvals where tenant_id='demo' and status='PENDING' order by created_at");}
 @PostMapping public Map<String,Object> create(@RequestBody Map<String,Object>b){db.update("insert into reminder_approvals(tenant_id,invoice_id,recipient,message) values('demo',?,?,?)",b.get("invoiceId"),b.get("recipient"),b.get("message"));return Map.of("queuedForReview",true);}
 @PostMapping("/{id}/approve")public Map<String,Object> approve(@PathVariable long id,@RequestBody Map<String,String>b){int n=db.update("update reminder_approvals set status='APPROVED',approved_by=?,approved_at=now() where id=? and tenant_id='demo' and status='PENDING'",b.getOrDefault("approvedBy","workspace-admin"),id);return Map.of("approved",n==1);}
 @PostMapping("/{id}/reject")public Map<String,Object> reject(@PathVariable long id){int n=db.update("update reminder_approvals set status='REJECTED' where id=? and tenant_id='demo' and status='PENDING'",id);return Map.of("rejected",n==1);}
}
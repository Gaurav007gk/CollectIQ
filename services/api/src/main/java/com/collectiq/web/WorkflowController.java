package com.collectiq.web;
import org.springframework.web.bind.annotation.*;import com.collectiq.security.TenantContext;import org.springframework.jdbc.core.JdbcTemplate;import java.util.*;
@RestController @RequestMapping("/api/workflows") public class WorkflowController{
 private final JdbcTemplate db;public WorkflowController(JdbcTemplate d){db=d;}
 @GetMapping public List<Map<String,Object>> list(){return db.queryForList("select id,name,enabled,cadence_hours,next_run_at from collection_workflows where tenant_id=? order by id",TenantContext.get());}
 @PostMapping public Map<String,Object> create(@RequestBody Map<String,Object>b){db.update("insert into collection_workflows(tenant_id,name,enabled,cadence_hours) values(?,?,true,?)",TenantContext.get(),b.get("name"),b.getOrDefault("cadenceHours",24));return Map.of("created",true);}
 @PostMapping("/{id}/run")public Map<String,Object> run(@PathVariable long id){int n=db.update("insert into collection_runs(tenant_id,workflow_id,status,started_at,details) select ?,id,'QUEUED',now,jsonb_build_object('trigger','manual') from collection_workflows where id=? and tenant_id=?",TenantContext.get(),id,TenantContext.get());return Map.of("queued",n>0);}
}
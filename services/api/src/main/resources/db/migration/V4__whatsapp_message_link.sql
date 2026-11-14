ALTER TABLE reminder_approvals ADD COLUMN external_message_id VARCHAR(180);
CREATE INDEX idx_approval_external_message ON reminder_approvals(external_message_id);
BEGIN TRANSACTION;
ALTER TABLE association_admins RENAME COLUMN association_id TO associations_id;
END TRANSACTION;
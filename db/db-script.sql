-- Schema for CIAMS project

-- Drop shcema inventory
DROP SCHEMA IF EXISTS inventory;

-- Create schema inventory
CREATE SCHEMA IF NOT EXISTS inventory
    AUTHORIZATION invuser;

-- Drop tables
DROP TABLE IF EXISTS inventory.asset_licence;
DROP TABLE IF EXISTS inventory.software_licence;
DROP TABLE IF EXISTS inventory.asset;
DROP TABLE IF EXISTS inventory.asset_type;
DROP TABLE IF EXISTS inventory.office;

-- Create table office
CREATE TABLE inventory.office (
	id SERIAL,
	name VARCHAR(100) NOT NULL
)

ALTER TABLE inventory.office ADD CONSTRAINT office_pk PRIMARY KEY (id);
ALTER TABLE inventory.office ADD CONSTRAINT office_name_unique UNIQUE (name);


-- Create table asset_type
CREATE TABLE inventory.asset_type (
	id SERIAL,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(200)
)

ALTER TABLE inventory.asset_type ADD CONSTRAINT asset_type_pk PRIMARY KEY (id);


-- Create table asset
CREATE TABLE inventory.asset (
	id SERIAL,
	serial_number VARCHAR(100) NOT NULL,
	acquisition_date DATE,
	office_id INTEGER NOT NULL,
	asset_type_id INTEGER NOT NULL
)

ALTER TABLE inventory.asset ADD CONSTRAINT asset_pk PRIMARY KEY (id);
ALTER TABLE inventory.asset ADD CONSTRAINT asset_sn_unique UNIQUE (serial_number);
ALTER TABLE inventory.asset ADD CONSTRAINT asset_office_fk FOREIGN KEY (office_id) REFERENCES inventory.office(id);
ALTER TABLE inventory.asset ADD CONSTRAINT asset_at_fk FOREIGN KEY (asset_type_id) REFERENCES inventory.asset_type(id);


-- Create table software_license
CREATE TABLE inventory.software_license (
	id SERIAL,
	name VARCHAR(100) NOT NULL,
	expire_date DATE NOT NULL
)

ALTER TABLE inventory.software_license ADD CONSTRAINT software_license_pk PRIMARY KEY (id);


-- Create relation table between asset and software_licence
CREATE TABLE inventory.asset_licence (
	asset_id INTEGER NOT NULL,
	licence_id INTEGER NOT NULL
)

ALTER TABLE inventory.asset_licence ADD CONSTRAINT asset_licence_pk PRIMARY KEY (asset_id, licence_id);
ALTER TABLE inventory.asset_licence ADD CONSTRAINT al_asset_fk FOREIGN KEY (asset_id) REFERENCES inventory.asset(id);
ALTER TABLE inventory.asset_licence ADD CONSTRAINT al_licence_fk FOREIGN KEY (licence_id) REFERENCES inventory.software_license(id);



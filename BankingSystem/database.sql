-- Customer table
CREATE TABLE customer (
    cust_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phoneno VARCHAR(15),
    city VARCHAR(50)
);

-- Branch table
CREATE TABLE branch (
    branch_code VARCHAR(10) PRIMARY KEY,
    branch_name VARCHAR(100) NOT NULL,
    branch_city VARCHAR(50)
);

-- Account table
CREATE TABLE account (
    account_no VARCHAR(20) PRIMARY KEY,
    cust_no VARCHAR(20),
    type VARCHAR(20),
    balance DOUBLE DEFAULT 0,
    branch_code VARCHAR(10),
    FOREIGN KEY (cust_no) REFERENCES customer(cust_no) ON DELETE CASCADE,
    FOREIGN KEY (branch_code) REFERENCES branch(branch_code)
);

-- Loan table
CREATE TABLE loan (
    loan_no VARCHAR(20) PRIMARY KEY,
    cust_no VARCHAR(20),
    amount DOUBLE,
    branch_code VARCHAR(10),
    FOREIGN KEY (cust_no) REFERENCES customer(cust_no) ON DELETE CASCADE,
    FOREIGN KEY (branch_code) REFERENCES branch(branch_code)
);

INSERT INTO customer (cust_no, name, phoneno, city) VALUES
('C0011', 'Elon Musk', '9999991111', 'California'),
('C0012', 'Mukesh Ambani', '9898998899', 'Mumbai'),
('C0013', 'Jeff Bezos', '7777773333', 'Washington'),
('C0014', 'Narayana Murthy', '8888884444', 'Bangalore'),
('C0015', 'Sundar Pichai', '6666665555', 'California');

INSERT INTO branch (branch_code, branch_name, branch_city) VALUES
('B001', 'Main Branch', 'Mumbai'),
('B002', 'Tech Hub', 'Bangalore'),
('B003', 'Silicon Valley', 'California'),
('B004', 'Capital City Branch', 'Delhi');

INSERT INTO account (account_no, cust_no, type, balance, branch_code) VALUES
('A0001', 'C0011', 'Savings', 1500000.00, 'B003'),
('A0002', 'C0012', 'Current', 2300000.00, 'B001'),
('A0003', 'C0013', 'Savings', 1200000.00, 'B003'),
('A0004', 'C0014', 'Current', 800000.00, 'B002'),
('A0005', 'C0015', 'Savings', 1750000.00, 'B003');

INSERT INTO loan (loan_no, cust_no, amount, branch_code) VALUES
('L0001', 'C0012', 7500000.00, 'B001'),
('L0002', 'C0014', 3000000.00, 'B002');
Delete from customer where cust_no='C0010';

select * from customer;


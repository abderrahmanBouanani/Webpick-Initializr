const express = require('express');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ message: 'Welcome to ${projectName} API!' });
});

<#if database?? && database == "POSTGRES">
// PostgreSQL Connection Example
const { Pool } = require('pg');
const pool = new Pool({
  connectionString: process.env.DATABASE_URL || 'postgresql://postgres:password@localhost:5432/${projectName}'
});

app.get('/db-test', async (req, res) => {
  try {
    const result = await pool.query('SELECT NOW()');
    res.json({ message: 'Connected to PostgreSQL!', time: result.rows[0].now });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});
<#elseif database?? && database == "MYSQL">
// MySQL Connection Example
const mysql = require('mysql2/promise');

app.get('/db-test', async (req, res) => {
  try {
    const connection = await mysql.createConnection(process.env.DATABASE_URL || 'mysql://root:password@localhost:3306/${projectName}');
    const [rows] = await connection.execute('SELECT NOW() as now');
    await connection.end();
    res.json({ message: 'Connected to MySQL!', time: rows[0].now });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});
<#elseif database?? && database == "MONGODB">
// MongoDB/Mongoose Connection Example
const mongoose = require('mongoose');

mongoose.connect(process.env.DATABASE_URL || 'mongodb://localhost:27017/${projectName}')
  .then(() => console.log('MongoDB Connected'))
  .catch(err => console.error('MongoDB Connection Error:', err));

app.get('/db-test', (req, res) => {
  if (mongoose.connection.readyState === 1) {
    res.json({ message: 'Connected to MongoDB!' });
  } else {
    res.status(500).json({ error: 'MongoDB is not connected' });
  }
});
</#if>

app.listen(port, () => {
  console.log(`Server running on port ${'$'}{port}`);
});

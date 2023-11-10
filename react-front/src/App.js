import React, { useState, useEffect } from 'react';
import Drivers from './components/Driver';
import DriverForm from './components/DriverForm';
import './App.css';

function App() {
  const [showForm, setShowForm] = useState(false);
  const [drivers, setDrivers] = useState([]);

  const addDriver = (newDriver) => {
    // Add the new driver to the list
    setDrivers([...drivers, newDriver]);

    // Clear the form and hide it
    setShowForm(false);
  };

  const handleShowForm = () => {
    setShowForm(true);
  };

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <Drivers drivers={drivers} />

        {/* Button to show the form */}
        {!showForm && <button onClick={handleShowForm}>Add Driver</button>}

        {/* Conditionally render the form */}
        {showForm && <DriverForm onAddDriver={addDriver} onCancel={() => setShowForm(false)} />}
      </header>
    </div>
  );
}

export default App;

import React, { useState } from 'react';
import Drivers from './components/Driver';
import DriverForm from './components/DriverForm';
import './App.css';

function App() {

  const [drivers, setDrivers] = useState([]);

  const addDriver = (newDriver) => {
    setDrivers([...drivers, newDriver]);
  };


  return (
    <div className="App">
      <header className="App-header">
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <Drivers />
        <Drivers drivers={drivers} />
        <DriverForm onAddDriver={addDriver} />
      </header>
    </div>
  );
}

export default App;

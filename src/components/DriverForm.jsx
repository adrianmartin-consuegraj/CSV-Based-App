import React, { useState } from 'react';

export default function DriverForm({ onAddDriver }) {
  const [name, setName] = useState('');
  const [points, setPoints] = useState('');

  const handleNameChange = (event) => {
    setName(event.target.value);
  };

  const handlePointsChange = (event) => {
    setPoints(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Create a new driver object
    const newDriver = {
      name,
      points: parseInt(points),
    };

    // Call your API endpoint to add the driver
    try {
      const response = await fetch('http://localhost:8080/addDriver', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newDriver),
      });

      if (response.ok) {
        // If the request was successful, add the new driver to the list
        onAddDriver(newDriver);
        // Clear the form fields
        setName('');
        setPoints('');
      } else {
        console.error('Error adding driver');
      }
    } catch (error) {
      console.error('Error adding driver:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Name:
        <input type="text" value={name} onChange={handleNameChange} required />
      </label>
      <br />
      <label>
        Points:
        <input type="number" value={points} onChange={handlePointsChange} required />
      </label>
      <br />
      <button type="submit">Add Driver</button>
    </form>
  );
}

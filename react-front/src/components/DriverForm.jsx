import React, { useState } from 'react';

export default function DriverForm({ onAddDriver, onCancel }) {
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

    // Call the API endpoint to add the driver
    try {
      const response = await fetch('http://localhost:8080/drivers/add', {
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
        // Hide the form
        onCancel();
      } else {
        console.error('Error adding driver');
      }
    } catch (error) {
      console.error('Error adding driver:', error);
    }
  };

  const handleCancel = () => {
    // Call onCancel function to hide the form
    onCancel();
    // Clear the form fields
    setName('');
    setPoints('');
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
      <button type="button" onClick={handleCancel}>
        Cancel
      </button>
    </form>
  );
}

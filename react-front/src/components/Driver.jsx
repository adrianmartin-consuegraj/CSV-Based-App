import React, { useEffect, useState } from 'react';

export default function Drivers() {
  const [data, setData] = useState(null);

  useEffect(() => {
    const apiUrl = 'http://localhost:8080/updateStandings'; // Replace with your actual API endpoint

    fetch(apiUrl)
      .then((response) => response.json())
      .then((responseData) => {
        setData(responseData);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
      });
  }, []);

  return (
    <div>
      <h1>Data from API Endpoint</h1>
      {data ? (
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Points</th>
            </tr>
          </thead>
          <tbody>
            {data.map((driver, index) => (
              <tr key={index}>
                <td>{driver.name}</td>
                <td>{driver.points}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Loading data...</p>
      )}
    </div>
  );
}
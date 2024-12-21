import React, { useState, useEffect, useRef } from 'react';
import './CreateRide.css';
import Header from '../../Components/Header/header';
import Footer from '../../Components/Footer/footer';
import L from 'leaflet';
import 'leaflet-routing-machine';

const CreateRide = () => {
    const [departure, setDeparture] = useState('');
    const [arrival, setArrival] = useState('');
    const [time, setTime] = useState('');
    const [seats, setSeats] = useState('');
    const [price, setPrice] = useState('');
    const [comment, setComment] = useState('');
    const [showModal, setShowModal] = useState(false);
    const mapRef = useRef(null);

    useEffect(() => {
        if (mapRef.current === null) {
            const map = L.map('map').setView([51.505, -0.09], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            mapRef.current = map;
        }
    }, []);

    const handleDepartureChange = (e) => {
        setDeparture(e.target.value);
    };

    const handleArrivalChange = (e) => {
        setArrival(e.target.value);
    };

    const handleDepartureKeyPress = (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            if (departure) {
                fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${departure}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.length === 0) {
                            alert('Departure location not found');
                            return;
                        }
                        const departureCoords = [data[0].lat, data[0].lon];
                        mapRef.current.setView(departureCoords, 13);
                        L.marker(departureCoords).addTo(mapRef.current);
                    });
            }
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!departure || !arrival || !time || !seats || !price) {
            alert('Please fill in all required fields.');
            return;
        }

        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${departure}`)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    alert('Departure location not found');
                    return;
                }
                const departureCoords = [data[0].lat, data[0].lon];
                fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${arrival}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.length === 0) {
                            alert('Arrival location not found');
                            return;
                        }
                        const arrivalCoords = [data[0].lat, data[0].lon];
                        L.Routing.control({
                            waypoints: [
                                L.latLng(departureCoords),
                                L.latLng(arrivalCoords)
                            ],
                            routeWhileDragging: true
                        }).addTo(mapRef.current);
                        setShowModal(true);
                    });
            });
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <div className='create-ride'>
            <Header />
            <div className='create-ride-banner'>
                <h1>OFFER A DRIVE AS A DRIVER</h1>
            </div>
            <div className='driverr'>
                <p>I am a driver</p>
                <p>Traject</p>
            </div>
            <div className='form-create-ride'>
                <form onSubmit={handleSubmit}>
                    <label>
                        Departure:
                        <input type="text" value={departure} onChange={handleDepartureChange} onKeyPress={handleDepartureKeyPress} />
                    </label>
                    <label>
                        Arrival:
                        <input type="text" value={arrival} onChange={handleArrivalChange} />
                    </label>
                    <label>
                        Time:
                        <input type="time" value={time} onChange={(e) => setTime(e.target.value)} />
                    </label>
                    <label>
                        Number of Seats:
                        <input type="number" value={seats} onChange={(e) => setSeats(e.target.value)} />
                    </label>
                    <label>
                        Price per Place:
                        <input type="number" value={price} onChange={(e) => setPrice(e.target.value)} />
                    </label>
                    <label>
                        Comment:
                        <textarea value={comment} onChange={(e) => setComment(e.target.value)} />
                    </label>
                    <button type="submit">Submit</button>
                </form>
                <div id="map" style={{ height: '400px', width: '100%' }}></div>
            </div>
            <Footer />
            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Traject saved successfully, all the users would be notified of your new post</h2>
                        <button onClick={handleCloseModal}>Close</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CreateRide;
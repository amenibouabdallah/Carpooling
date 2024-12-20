import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './GetDrive.css';
import Header from '../../Components/Header/header';
import Footer from '../../Components/Footer/footer';
import DriveCard from '../../Components/DriveCard/DriveCard';
import Modal from '../../Components/Modal/Modal';

const GetDrive = () => {
    const navigate = useNavigate();
    const [depart, setDepart] = useState('');
    const [destination, setDestination] = useState('');
    const [date, setDate] = useState('');
    const [prix, setPrix] = useState('');
    const [drives, setDrives] = useState([]);
    const [filteredDrives, setFilteredDrives] = useState([]);
    const [selectedDrive, setSelectedDrive] = useState(null);

    const handleFindRide = () => {
        if (!depart && !destination && !date && !prix) {
            alert('At least one field needs to be filled');
        } else {
            const filtered = drives.filter(drive => {
                return (
                    (depart ? drive.departurePoint.toLowerCase().includes(depart.toLowerCase()) : true) &&
                    (destination ? drive.arrivalPoint.toLowerCase().includes(destination.toLowerCase()) : true) &&
                    (date ? drive.dateTime.startsWith(date) : true) &&
                    (prix ? drive.pricePerPlace <= parseFloat(prix) : true)
                );
            });
            setFilteredDrives(filtered);
        }
    };

    useEffect(() => {
        // Dummy data for testing
        const dummyDrives = [
            {
                id: 1,
                driverName: 'John Doe',
                departurePoint: 'Tunis',
                dateTime: '2023-10-15T08:00',
                possiblePlaces: 3,
                pricePerPlace: 20.0,
                comment: 'No smoking in the car, please.',
                arrivalPoint: 'Sousse'
            },
            {
                id: 2,
                driverName: 'Jane Smith',
                departurePoint: 'Sfax',
                dateTime: '2023-10-16T09:00',
                possiblePlaces: 2,
                pricePerPlace: 15.0,
                comment: 'Pets are allowed.',
                arrivalPoint: 'Gabes'
            },
            {
                id: 3,
                driverName: 'Alice Johnson',
                departurePoint: 'Nabeul',
                dateTime: '2023-10-17T10:00',
                possiblePlaces: 4,
                pricePerPlace: 25.0,
                comment: 'No loud music.',
                arrivalPoint: 'Hammamet'
            },
            {
                id: 4,
                driverName: 'Bob Brown',
                departurePoint: 'Monastir',
                dateTime: '2023-10-18T11:00',
                possiblePlaces: 1,
                pricePerPlace: 30.0,
                comment: 'Air conditioning available.',
                arrivalPoint: 'Mahdia'
            },
            {
                id: 5,
                driverName: 'Charlie Davis',
                departurePoint: 'Bizerte',
                dateTime: '2023-10-19T12:00',
                possiblePlaces: 3,
                pricePerPlace: 18.0,
                comment: 'No food in the car.',
                arrivalPoint: 'Beja'
            },
            {
                id: 6,
                driverName: 'Diana Evans',
                departurePoint: 'Kairouan',
                dateTime: '2023-10-20T13:00',
                possiblePlaces: 2,
                pricePerPlace: 22.0,
                comment: 'Friendly driver.',
                arrivalPoint: 'Kasserine'
            },
            {
                id: 7,
                driverName: 'Eve Foster',
                departurePoint: 'Gafsa',
                dateTime: '2023-10-21T14:00',
                possiblePlaces: 4,
                pricePerPlace: 20.0,
                comment: 'No pets allowed.',
                arrivalPoint: 'Tozeur'
            },
            {
                id: 8,
                driverName: 'Frank Green',
                departurePoint: 'Douz',
                dateTime: '2023-10-22T15:00',
                possiblePlaces: 3,
                pricePerPlace: 19.0,
                comment: 'Comfortable seats.',
                arrivalPoint: 'Matmata'
            },
            {
                id: 9,
                driverName: 'Grace Harris',
                departurePoint: 'Zarzis',
                dateTime: '2023-10-23T16:00',
                possiblePlaces: 2,
                pricePerPlace: 21.0,
                comment: 'No smoking.',
                arrivalPoint: 'Djerba'
            },
            {
                id: 10,
                driverName: 'Henry Irving',
                departurePoint: 'Medinine',
                dateTime: '2023-10-24T17:00',
                possiblePlaces: 1,
                pricePerPlace: 24.0,
                comment: 'Fast driver.',
                arrivalPoint: 'Tataouine'
            }
        ];
        setDrives(dummyDrives);
        setFilteredDrives(dummyDrives); // Initialize filtered drives with all drives
    }, []);

    const handleCardClick = (drive) => {
        setSelectedDrive(drive);
    };

    const handleCloseModal = () => {
        setSelectedDrive(null);
    };

    const handleConfirmRide = () => {
        alert('Ride confirmed!');
        setSelectedDrive(null);
    };

    return (
        <div>
            <Header />
            <div className="background-image">
                <div className="overlay">
                    <h1>Find Your Ride</h1>
                    <div className="search-bar">
                        <input type="text" placeholder="Depart" value={depart} onChange={(e) => setDepart(e.target.value)} />
                        <input type="text" placeholder="Destination" value={destination} onChange={(e) => setDestination(e.target.value)} />
                        <input type="date" placeholder="Date" value={date} onChange={(e) => setDate(e.target.value)} />
                        <input type="number" placeholder="Prix" value={prix} onChange={(e) => setPrix(e.target.value)} />
                    </div>
                    <div className="buttons">
                        <button onClick={() => navigate('/offer-ride')}>Offer a Ride</button>
                        <button onClick={handleFindRide}>Find a Ride</button>
                    </div>
                </div>
            </div>
            <div className='drive-content'>
                {filteredDrives.map(drive => (
                    <DriveCard key={drive.id} drive={drive} onClick={handleCardClick} />
                ))}
            </div>
            {selectedDrive && (
                <Modal 
                    drive={selectedDrive} 
                    onClose={handleCloseModal} 
                    onConfirm={handleConfirmRide} 
                />
            )}
            <div className="infos-banner">
                <h1>
                TO WORK, FESTIVALS AND MORE
                </h1>
                <div className='infos-banner-content'>
                    <p>
                    Driving together allows you to share costs, makes driving more fun and helps protect the environment.
                    </p>
                    <div className='separator'></div>
                    <p>
                    With us you’ll find your carpool partner for all your journeys in Tunisia. Place a drive and Carpool will find the best match for you. Make arrangements and don’t forget to enjoy your ride!
                    </p>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default GetDrive;
import React from 'react';
import './History.css';
import Header from '../../Components/Header/header';
import Footer from '../../Components/Footer/footer';
import HistoryCard from '../../Components/HistoryCard/HistoryCard';

const dummyData = [
    {
        id: 1,
        driverId: 101,
        departurePoint: 'Point A',
        dateTime: '2023-10-01T10:00:00',
        possiblePlaces: 3,
        pricePerPlace: 15.0,
        comment: 'No smoking',
        arrivalPoint: 'Point B'
    },
    {
        id: 2,
        driverId: 102,
        departurePoint: 'Point C',
        dateTime: '2023-10-02T12:00:00',
        possiblePlaces: 2,
        pricePerPlace: 20.0,
        comment: 'Pets allowed',
        arrivalPoint: 'Point D'
    },
    {
        id: 3,
        driverId: 103,
        departurePoint: 'Point E',
        dateTime: '2023-10-03T14:00:00',
        possiblePlaces: 4,
        pricePerPlace: 10.0,
        comment: 'No luggage',
        arrivalPoint: 'Point F'
    }
];

const History = () => {
    return (
        <div className="history">
            <Header />
            <h1>Welcome Back</h1>
            <p>Let's check your history</p>
            <div className="history-cards">
                {dummyData.map((data) => (
                    <HistoryCard
                        key={data.id}
                        id={data.id}
                        driverId={data.driverId}
                        departurePoint={data.departurePoint}
                        dateTime={data.dateTime}
                        possiblePlaces={data.possiblePlaces}
                        pricePerPlace={data.pricePerPlace}
                        comment={data.comment}
                        arrivalPoint={data.arrivalPoint}
                    />
                ))}
            </div>
            <Footer />
        </div>
    );
};

export default History;
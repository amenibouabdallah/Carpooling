import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import './GetDrive.css';
import Header from '../../Components/Header/header';
import Footer from '../../Components/Footer/footer';
import tunisia from "../../assets/tunis.jpg";

const GetDrive = () => {
    const navigate = useNavigate();
    return (
        <div>
            <Header />
            <div className="background-image">
                <div className="overlay">
                    <h1>Find Your Ride</h1>
                    <div className="search-bar">
                        <input type="text" placeholder="Depart" />
                        <input type="text" placeholder="Destination" />
                        <input type="date" placeholder="Date" />
                        <input type="number" placeholder="Prix" />
                    </div>
                    <div className="buttons">
                        <button onClick={() => navigate('/offer-ride')}>Offer a Ride</button>
                        <button onClick={() => navigate('/find-ride')}>Find a Ride</button>
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default GetDrive;
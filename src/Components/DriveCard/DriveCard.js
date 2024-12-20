import React from 'react';
import './DriveCard.css';
import { BsFillPersonFill } from "react-icons/bs";

const DriveCard = ({ drive, onClick }) => {
    return (
        <div className="drive-card" onClick={() => onClick(drive)}>
            <div className='icon-driver'>
                <BsFillPersonFill id='icon'/>
            </div>
            <div className='name-time-date'>
                <h2>{drive.driverName}</h2>
                <p id='time-date'>{drive.dateTime}</p>
                <p id='driver-text'>Driver</p>
            </div>
            <div className='departure-arrival'>
                <p>{drive.departurePoint}</p>
                <p>{drive.arrivalPoint}</p>
            </div>
            <div className='price-places'>
                <p>${drive.pricePerPlace}</p>
                <p>{drive.possiblePlaces} places</p>
            </div>
            <div className='comment'>
                <p>{drive.comment}</p>
            </div>
        </div>
    );
};

export default DriveCard;
import React from 'react';
import './Profile.css';
import Header from '../../Components/Header/header';
import Footer from '../../Components/Footer/footer';
import avatar from '../../assets/avatar.png';

const Profile = () => {
    // Dummy user data for testing
    const user = {
        name: 'John Doe',
        phoneNumber: '123456789',
        email: 'john.doe@example.com',
        address: '123 Main St, Anytown, USA',
        isVerified: true,
        role: 'User'
    };

    return (
        <div className="profile-container">
           <Header />
           <div className='avatar-container'>
            <h3 className='profile'>MY PROFILE</h3>
            <img src={avatar} alt='avatar' className='avatar'/>
           </div>
           <div className='profile-info'>
            <div className='name-phone'>
            <div className='name'>
                <p> Full Name</p>
                <div className='name-input'>{user.name}</div>
            </div>
            <div className='phone'>
                <p>Phone Number</p>
                <div className='phone-input'>{user.phoneNumber}</div>
            </div>
            </div>
            <div className='email-address'>
            <div className='email'>
                <p>Email</p>
                <div className='email-input'>{user.email}</div>
            </div>
            <div className='address'>
                <p>Address</p>
                <div className='address-input'>{user.address}</div>
            </div>
            </div>
            <div className='verified-role'>
            <div className='role'>
                <p>Role</p>
                <div className='role-input'>{user.role}</div>
            </div>
            <div className='verified'>
                <p>Verified</p>
                <div className='verified-input'>{user.isVerified ? 'Yes' : 'No'}</div>
            </div>
            </div>
            </div>
            <Footer />
        </div>
    );
};

export default Profile;
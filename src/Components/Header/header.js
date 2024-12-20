import React from 'react';
import './header.css';
import logo from "../../assets/logo.png";

const Header = () => {
    return (
        <nav className="navbar">
            <div className="logo"><img src={logo} alt="logo" id='logo'/></div>
            <ul className="nav-links">
                <li><a href="/" className={window.location.pathname === "/" ? "active" : ""}>Home</a></li>
                <li><a href="/get-drive" className={window.location.pathname === "/get-drive" ? "active" : ""}>Get Drive</a></li>
                <li><a href="/contact" className={window.location.pathname === "/contact" ? "active" : ""}>Contact Us</a></li>
                <li><a href="/policy" className={window.location.pathname === "/policy" ? "active" : ""}>Policy</a></li>
                <li><a href="/login">Login</a></li>
                <li><a href="/signup">Signup</a></li>
            </ul>
        </nav>
    );
};

export default Header;
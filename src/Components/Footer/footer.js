import React from 'react';

const footerStyle = {
    backgroundColor: '#0063db',
    color: 'white',
    fontFamily: 'Roboto, sans-serif',
    padding: '20px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '20px',
    fontWeight: 300
};

const Footer = () => {
    return (
        <footer id="footer" style={footerStyle}>
            <p>© 2024 Website created by Amen Boughalmi and Ameny Bouabdallah khalil feguir</p>
        </footer>
    );
};

export default Footer;
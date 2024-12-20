import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../Components/Header/header";
import "./home.css";
import { CiLogin } from "react-icons/ci";
import { AiFillSchedule } from "react-icons/ai";
import { FaCar } from "react-icons/fa";
import iphone from "../../assets/iphone.png";
import Footer from "../../Components/Footer/footer";

const Home = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const title = document.querySelector('.how-it-works-title');
    const steps = document.querySelectorAll('.how-it-works-step');
    const geoMapContent = document.querySelector('.geo-map-content');
    const iphone = document.getElementById('iphone');
    const circles = document.querySelectorAll('.circle');

    function isElementInViewport(el) {
      const rect = el.getBoundingClientRect();
      return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.right <= (window.innerWidth || document.documentElement.clientWidth)
      );
    }

    function onScroll() {
      if (isElementInViewport(title)) {
        title.classList.add('fade-in');
      }

      steps.forEach((step, index) => {
        if (isElementInViewport(step)) {
          setTimeout(() => {
            step.classList.add('fade-in');
          }, index * 300); // Adjust the delay as needed
        }
      });

      if (isElementInViewport(geoMapContent)) {
        geoMapContent.classList.add('fade-in');
      }
    }

    function onMouseMove(event) {
      const { clientX, clientY } = event;
      const { innerWidth, innerHeight } = window;
      const moveX = (clientX / innerWidth - 0.5) * -20; // Adjust the multiplier as needed
      const moveY = (clientY / innerHeight - 0.5) * -20; // Adjust the multiplier as needed
      iphone.style.transform = `translate(${moveX}px, ${moveY}px)`;
      circles.forEach((circle, index) => {
        const circleMoveX = (clientX / innerWidth - 0.5) * -10 * (index + 1); // Adjust the multiplier as needed
        const circleMoveY = (clientY / innerHeight - 0.5) * -10 * (index + 1); // Adjust the multiplier as needed
        circle.style.transform = `translate(${circleMoveX}px, ${circleMoveY}px)`;
      });
    }

    window.addEventListener('scroll', onScroll);
    iphone.addEventListener('mousemove', onMouseMove);
    onScroll(); // Trigger the function on load

    return () => {
      window.removeEventListener('scroll', onScroll);
      iphone.removeEventListener('mousemove', onMouseMove);
    };
  }, []);

  const scrollToHowItWorks = () => {
    const howItWorksSection = document.querySelector('.how-it-works');
    if (howItWorksSection) {
      howItWorksSection.scrollIntoView({ behavior: 'smooth' });
    }
  };

  const navigateToSignup = () => {
    navigate('/signup');
  };

  return (
    <div className="home-container">
      <Header />
      <div className="home-introduction">
        <h1 className="home-title">Connecting drivers and passengers in Tunisia!</h1>
        <p className="home-description">FiThnitek brings together drivers and passengers in a local way, facilitating trips across The Blue Mile and beyond.</p>
        <button id="start-button-home" onClick={scrollToHowItWorks}>Let's Get You Started</button>
      </div>
      <div className="how-it-works">
        <h2 className="how-it-works-title">How It Works</h2>
        <div className="how-it-works-steps">
          <div className="how-it-works-step">
            <CiLogin id="login-icon" />
            <h3>Sign Up</h3>
            <p>Sign up as a driver or passenger by providing your information.</p>
          </div>
          <div className="how-it-works-step">
            <AiFillSchedule id="schedule-icon" />
            <h3>Book or Schedule</h3>
            <p>Ride now, or some time in the future. We'll link up drivers and passengers and help with payments.</p>
          </div>
          <div className="how-it-works-step">
            <FaCar id="car-icon" />
            <h3>Get There</h3>
            <p>Reach your destination quickly and safely</p>
          </div>
        </div>
      </div>
      <div className="geo-maps">
        <div className="geo-map-content">
          <h2>Tell us where you want to go, we'll handle the rest.</h2>
          <p>We share your request with our network of local drivers and handle payments and booking. We're also here to help if you need support, keeping a close eye on operations during peak hours.</p>
        </div>
        <div className="geo-map-image">
          <img src={iphone} alt="iphone" id="iphone" />
          <div className="circle circle1"></div>
          <div className="circle circle2"></div>
          <div className="circle circle3"></div>
        </div>
      </div>
      <div className="join-driver-signup-home">
        <h2>Interested in becoming a driver?</h2>
        <p>We're currently looking for new drivers to help us test BoroXpress. Be the first to get on board.</p>
        <button id="signup-button-home" onClick={navigateToSignup}>Sign Up</button>
      </div>
      <Footer />
    </div>
  );
};

export default Home;

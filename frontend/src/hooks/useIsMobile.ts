import React from "react";

export const useIsMobile = () => {
  const [isMobile, setIsMobile] = React.useState(false);

  React.useEffect(() => {
    const checkIsMobile = () => {
      setIsMobile(document.body.clientWidth < 767);
    };

    window.addEventListener("resize", checkIsMobile);
    checkIsMobile();
  }, []);

  return isMobile;
};

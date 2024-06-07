export function getCurrentTimeString(time: string) {
  let currentDate = new Date(time);

  let hours: string | number = currentDate.getHours();
  let minutes: string | number = currentDate.getMinutes();

  hours = hours < 10 ? "0" + hours : hours;
  minutes = minutes < 10 ? "0" + minutes : minutes;

  let timeString = hours + ":" + minutes;

  return timeString;
}

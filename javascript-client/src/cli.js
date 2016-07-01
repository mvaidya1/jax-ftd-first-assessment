'use strict'
import vorpal from 'vorpal'
import net from 'net'
import fs from 'fs'
import { hash, compare } from './hashing'

const cli = vorpal()
let server
const host = '127.0.0.1'
const port = 667
const users = {}
let Userlogged = false// By default false it changes true when a User is logged-in
/*
*Connects With the Client with the Server
*/
cli
.delimiter('connected:')
server = net.createConnection(port, host, () => {
  return 0
})
/*
 * Disconnects the Client from the Server
*/
server.on('end', () => {
  this.log('disconnected from server :(')
  cli.exec('exit')
})
const writeTo = (string) => server.write(string + '\n')

const writeJSONFile = (object) => {
  server.write(JSON.stringify({ 'files': object }) + '\n')
}
const createFile = (filepath, buffer, username) => {
  return {
    'filePath': filepath,
    'buffer': buffer,
    'username': username
  }
}
/*
register <username> <password>
    * registers a new user with the application
    * the plaintext password willn't be stored anywhere, ever
    * the password will be hashed for storage according to the bcrypt algorithm
*/
const register = cli.command('register <username> <password>')
register
.description('Registers a user with this application')
.action(function (args, callback) {
  return (
    Promise.resolve(users[args.username] !== undefined)
    .then(
      (alreadyRegistered) =>
       alreadyRegistered
       ? this.log('Username already Registered ! Choose another name')
       : hash(args.password)
              .then((hashedPassword) => users[args.username] = hashedPassword)
              .then(() => this.log('Registration successful!'))
      )
      .catch((err) => this.log(`An error occurred: ${err}`))
  )
})
/*
login <username> <password>
    * logs a user in to the command line interface
    * the plaintext password willn't be stored anywhere, ever
    * the commandline application should store the user details necessary for logged-in functionality
    * the hashed password will be compared on the client side
*/
const login = cli.command('login <username> <password>')
login
  .description('Login with a specified username and password')
  .action(function (args, callback) {
    return (
      Promise.resolve(users[args.username])
        .then(
          (hashedPassword) =>
            hashedPassword === undefined
              ? this.log('Username or password incorrect. Try again!')
              : compare(args.password, hashedPassword)
                .then((correctPassword) => {
                  if (correctPassword) {
                    Userlogged = args.username
                    ? this.log('Successfully logged in!')
                    : this.log('Username or password incorrect. Try again!')
                  }
                })
        )
        .catch((err) => this.log(`An error occurred: ${err}`))
    )
  })
// DisconnectServer()
/* logout
*  logsout if the user is logged-in
*/
cli
  .command('logout')
  .description('Logs you out, if you are logged in')
  .action((callback) => {
    if (!Userlogged) {
      cli.log('You are not logged in. You have to be logged in to log out.')
      server.close()
    } else {
      Userlogged = false
    }
    callback()
  })
    // Checks whether the user is logged-in
const files = cli.command('Files')
files
.description('Displays list of files')
.action((args, callback) => {
  if (Userlogged) {
    cli.log('You are not logged in. You have to be logged in to access')
  } else {
  //  ServerConnection()
    writeTo(`getlist ${Userlogged}`)
    server.on('data', (d) => {
      cli.log(d.toString())
    })
  //  DisconnectServer()
    callback()
  }
})

const upload = cli.command('upload <localFilePath> [pathStoredInTheDatabase]')
upload
.description('Uploads the local file paths')
.description('Upload a file to you')
  .action((args, callback) => {
    if (!Userlogged) {
      cli.log('You are not logged in. You have to be logged in to access')
    } else {
      let filePathToUpload
      if (!args.pathfordatabase) {
        filePathToUpload = args.absolutefilepath
      } else {
        filePathToUpload = args.pathfordatabase
      }
    //  ServerConnection()
      fs.open(args.absolutefilepath, 'r', (err, fd) => {
        if (err) {
          cli.log(`There was an error opening the file to send: ${err}`)
        }
        let buffer = new Buffer.alloc(fs.statSync(args.absolutefilepath).size)
        fs.read(fd, buffer, 0, buffer.length, 0, (err, num) => {
          if (err) {
            cli.log(`There was an error opening the file to send: ${err}`)
          }
          writeJSONFile(createFile(filePathToUpload, buffer.toString('base64'), Userlogged))
          if (err) throw err
        })
      })
    }
  //  DisconnectServer()
    callback()
  })
    // Checks whether the user is logged-im
const download = cli.command('download <databaseFileId> [localFilePath]')
download
.description('Downloads file from your database')
.action((args, callback) => {
  if (!Userlogged) {
    cli.log('You are not logged in. You have to be logged in to access')
  } else {
  //  ServerConnection()
    writeTo(`getfile ${args.fileid}`)
    server.on('data', (data) => {
      let filePathToSave
      let parsed = JSON.parse((data.toString()))
      if (!args.filepath) {
        filePathToSave = parsed.files.filePath
      } else {
        filePathToSave = args.filepath
      }
      let buffer = Buffer.from(parsed.files.buffer, 'base64')
      fs.open(filePathToSave, 'w', (err, fd) => {
        if (err) cli.log(`Error opening file to write file: ${err}`)
        fs.write(fd, buffer, 0, buffer.length, null, (err) => {
          if (err) cli.log(`Error writing to file: ${err}`)
          fs.close(fd, () => {
            cli.log(`File written to ${filePathToSave}!`)
          })
        })
      })
    })
  }
//  DisconnectServer()
  callback()
})
export default cli

<?php

$hit_count = @file_get_contents('checkCount.txt');
$hit_count++;
@file_put_contents('checkCount.txt', $hit_count);

header('Location: remote_version.txt'); // redirect to the real file to be downloaded

<?php

$hit_count = @file_get_contents('updateCount.txt');
$hit_count++;
@file_put_contents('updateCount.txt', $hit_count);

header('Location: latest.txt'); // redirect to the real file to be downloaded
